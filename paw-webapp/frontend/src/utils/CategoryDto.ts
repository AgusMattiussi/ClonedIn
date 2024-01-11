export default interface CategoryDto {
  id: number
  name: string
  links: CategoryDtoLinks
}

interface CategoryDtoLinks {
  self: string
}
