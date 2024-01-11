export default interface SkillDto {
  id: number
  description: string
  links: SkillDtoLinks
}

interface SkillDtoLinks {
  self: string
}
