import { useRequestApi } from "../api/apiRequest"

export const useGetCategoryByUrl = () => {
  const { apiRequest } = useRequestApi()

  async function getCategoryByUrl(categoryUrl: string) {
    const response = await apiRequest({
      url: categoryUrl,
      method: "GET",
    })
    return response
  }

  return { getCategoryByUrl }
}
