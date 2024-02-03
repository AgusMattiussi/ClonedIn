import { useRequestApi } from "../api/apiRequest"

export const useGetUserEducations = () => {
  const { apiRequest } = useRequestApi()

  async function getUserEducations(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}/educations`,
      method: "GET",
      requiresAuth: true,
    })
    return response
  }

  return { getUserEducations }
}
