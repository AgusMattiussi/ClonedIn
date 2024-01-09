export default interface JobOfferDto {
  id: number
  position: string
  description: string
  salary: string
  modality: string
  available: string
  links: JobOfferDtoLinks
}

interface JobOfferDtoLinks {
  self: string
  enterprise: string
  category: string
  skills: []
}
