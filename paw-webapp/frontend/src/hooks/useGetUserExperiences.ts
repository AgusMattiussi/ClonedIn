import { useRequestApi } from "../api/apiRequest"

export const useGetUserExperiences = () => {
  const { apiRequest } = useRequestApi()

  async function getUserExperiences(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}/experiences`,
      method: "GET",
      requiresAuth: true,
    })
    return response
  }

  return { getUserExperiences }
}
