import UserDto from "./UserDto"
import EnterpriseDto from "./EnterpriseDto"
import JobOfferDto from "./JobOfferDto"
import CategoryDto from "./CategoryDto"

export default interface ContactDto {
  status: string
  filledBy: number
  date: string
  userInfo: UserDto
  userName: string
  userYearsOfExp: number
  userId: number
  categoryInfo: CategoryDto
  enterpriseInfo: EnterpriseDto
  jobOfferInfo: JobOfferDto
  links: ContactDtoLinks
}

interface ContactDtoLinks {
  self: string
  user: string
  enterprise: string
  jobOffer: string
  userCategory: string
}
