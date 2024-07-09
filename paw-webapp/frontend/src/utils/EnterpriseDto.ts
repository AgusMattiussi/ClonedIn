import CategoryDto from "./CategoryDto"

export default interface EnterpriseDto {
  id: number
  name: string
  email: string
  location: string
  workers: string
  yearFounded: number
  website: string
  description: string
  categoryInfo: CategoryDto
  links: EnterpriseDtoLinks
}

interface EnterpriseDtoLinks {
  self: string
  image: string
  category: string
}
