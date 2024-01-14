import CategoryDto from "./CategoryDto"

export default interface JobOfferDto {
  id: number
  position: string
  description: string
  salary: string
  modality: string
  available: string
  categoryInfo: CategoryDto
  links: JobOfferDtoLinks
}

interface JobOfferDtoLinks {
  self: string
  enterprise: string
  category: string
  skills: string
}
