export default interface EnterpriseDto {
  id: number
  name: string
  email: string
  location: string
  workers: string
  year: number
  website: string
  description: string
  links: EnterpriseDtoLinks
}

interface EnterpriseDtoLinks {
  self: string
  image: string
  category: string
}
