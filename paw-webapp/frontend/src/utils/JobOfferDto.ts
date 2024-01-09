export default interface JobOfferDto {
  id: number
  position: string
  description: string
  salary: string
  modality: string
  available: string
  skills: []
  links: JobOfferDtoLinks
}

interface JobOfferDtoLinks {
  self: string
  enterprise: string
  category: string
}
