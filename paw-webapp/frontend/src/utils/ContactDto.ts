import UserDto from "./UserDto"
import EnterpriseDto from "./EnterpriseDto"
import JobOfferDto from "./JobOfferDto"

export default interface ContactDto {
  status: string
  filledBy: number
  date: string
  userInfo: UserDto
  enterpriseInfo: EnterpriseDto
  jobOfferInfo: JobOfferDto
  links: ContactDtoLinks
}

interface ContactDtoLinks {
  self: string
  user: string
  enterprise: string
  jobOffer: string
}
