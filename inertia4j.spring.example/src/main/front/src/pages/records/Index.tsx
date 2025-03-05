import { FormEvent } from 'react'
import { Record } from './types'
import { useForm } from '@inertiajs/react'

export default function Index({ records }: { records: Record[] }) {
  const { post } = useForm({ id: 1, name: 'New record' })

  const submit = (e: FormEvent) => {
    e.preventDefault()
    post('/records')
  }

  return (
    <>
      <h1>You got {records.length} records.</h1>
      <form onSubmit={submit}>
        <button type="submit">Create new</button>
      </form>
    </>
  )
}
