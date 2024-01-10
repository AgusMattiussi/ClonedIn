import CategoryDto from "./CategoryDto"

export default interface UserDto {
  id: number
  email: string
  name: string
  location: string
  currentPosition: string
  description: string
  educationLevel: string
  visibility: number
  categoryInfo: CategoryDto
  links: UserDtoLinks
}

interface UserDtoLinks {
  self: string
  image: string
  category: string
  experiences: string
  educations: string
  skills: string
}
