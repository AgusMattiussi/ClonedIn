import CategoryDto from "./CategoryDto"
import ExperienceDto from "./ExperienceDto"
import SkillDto from "./SkillDto"

export default interface UserDto {
  id: number
  email: string
  name: string
  location: string
  currentPosition: string
  description: string
  educationLevel: string
  visible: boolean
  categoryInfo: CategoryDto
  experienceInfo: ExperienceDto[]
  skillsInfo: SkillDto[]
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
