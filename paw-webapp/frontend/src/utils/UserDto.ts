export default interface UserDto {
  id: number
  name: string
  email: string
  location: string
  category: string
  currentPosition: string
  description: string
  education: string
  visibility: number

  self: string
  userSkills: string
  educations: string
  experiences: string
  //TODO: Add URIs for other resources (experiences, education, image, userSkills, contacts)
}
