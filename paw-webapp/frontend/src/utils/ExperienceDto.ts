export default interface ExperienceDto {
  id: number
  position: string
  enterpriseName: string
  description: string
  monthFrom: number
  yearFrom: number
  monthTo: number
  yearTo: number
  links: ExperienceDtoLinks
}

interface ExperienceDtoLinks {
  self: string
  user: string
}
