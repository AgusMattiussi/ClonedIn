import { useRequestApi } from "../api/apiRequest"

export const useGetByUrl = () => {
  const { apiRequest } = useRequestApi()

  async function getUserByUrl(userUrl: string) {
    const response = await apiRequest({
      url: userUrl,
      method: "GET",
      requiresAuth: true,
    })
    return response
  }

  return { getUserByUrl }
}
