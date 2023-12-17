export default interface JobOfferDto {
  id: number
  name: string
  category: string
  description: string
  position: string
  modality: string
  salary: string
  skills: []
  //TODO: Add URIs for other resources (experiences, education, image, userSkills, contacts)
}
