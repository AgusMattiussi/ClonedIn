import { useRequestApi } from "../api/apiRequest"

export const useGetUserSkills = () => {
  const { apiRequest } = useRequestApi()

  async function getUserSkills(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}/skills`,
      method: "GET",
      requiresAuth: true,
    })
    return response
  }

  return { getUserSkills }
}
