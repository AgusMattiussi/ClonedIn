import { useRequestApi } from "../api/apiRequest"

export const useGetUserData = () => {
  const { apiRequest } = useRequestApi()

  async function getUser(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "GET",
    })
    return response
  }

  async function getUserExperiences(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}/experiences`,
      method: "GET",
    })
    return response
  }

  async function getUserEducations(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}/educations`,
      method: "GET",
    })
    return response
  }

  async function getUserSkills(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}/skills`,
      method: "GET",
    })
    return response
  }

  return { getUser, getUserExperiences, getUserEducations, getUserSkills }
}
