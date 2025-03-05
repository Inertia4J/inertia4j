import { Record } from './types'

export default function Show({ record }: { record: Record }) {
  return <h1>Record: {record.name}</h1>
}
