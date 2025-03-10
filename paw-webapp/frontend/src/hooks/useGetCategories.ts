import { useRequestApi } from "../api/apiRequest"

export const useGetCategories = () => {
  const { apiRequest } = useRequestApi()

  async function getCategories() {
    const response = await apiRequest({
      url: "/categories",
      method: "GET",
    })
    return response
  }

  return { getCategories }
}
