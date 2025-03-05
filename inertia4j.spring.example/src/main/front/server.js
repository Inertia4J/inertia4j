import express from 'express'
import { createServer as createViteServer, createLogger } from 'vite'
import minimist from 'minimist'

const args = minimist(process.argv.slice(2))
const upstreamUrl = args.u || 'http://localhost:8080'
const port = args.p || 5173

const logger = createLogger()

const toHeadersObject = (headers) => {
  const headersObject = {}
  for (const [key, value] of headers.entries()) {
    headersObject[key] = value
  }
  return headersObject
}

/**
 * Custom dev server to allow fetching the base HTML from the backend server.
 * 
 * See https://vite.dev/guide/ssr.html#setting-up-the-dev-server for details.
 */
async function createServer() {
  const app = express()
  
  const vite = await createViteServer({
    server: { middlewareMode: true },
    appType: 'custom'
  })

  app.use(vite.middlewares)
  app.use(express.json())
  app.use('*', async (req, res, next) => {
    const url = req.originalUrl
  
    try {
      const extraFetchOptions = req.method === 'GET' || req.method === 'HEAD'
        ? {}
        : { body: JSON.stringify(req.body) }

      const response = await fetch(upstreamUrl + url, { 
        method: req.method,
        headers: req.headers,
        ...extraFetchOptions
      })

      logger.info(`[${response.status}] ${req.method} ${url} -> ${upstreamUrl}${url}`)

      if (response.headers.get('content-type')?.startsWith('text/html')) {
        const template = await response.text()
        const html = await vite.transformIndexHtml(url, template)
    
        res.status(200).set({ 'Content-Type': 'text/html' }).end(html)
      } else {
        const headers = toHeadersObject(response.headers)

        res.status(response.status).set(headers).send(await response.text())
      }
    } catch (e) {
      vite.ssrFixStacktrace(e)
      next(e)
    }
  })

  app.listen(port)

  logger.info(`Server running at http://localhost:${port}`)
}

createServer()
