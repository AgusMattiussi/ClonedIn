import { useRequestApi } from "../api/apiRequest"

export const useGetUserData = () => {
  const { apiRequest } = useRequestApi()

  async function getUsers(queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/users`,
      method: "GET",
      queryParams: queryParams,
    })
    return response
  }

  async function getUserById(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "GET",
    })
    return response
  }

  async function getUserByUrl(userUrl: string) {
    const response = await apiRequest({
      url: userUrl,
      method: "GET",
    })
    return response
  }

  //Usamos este hook para obtener info como la cantidad de años de xp de un usuario y sus skills en enterpriseInterested.tsx
  async function getUserExtraInfo(url: string) {
    const response = await apiRequest({
      url: url,
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

  async function getUserContacts(id: string | undefined, queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/users/${id}/contacts`,
      method: "GET",
      queryParams: queryParams,
    })
    return response
  }

  return {
    getUsers,
    getUserById,
    getUserByUrl,
    getUserExtraInfo,
    getUserExperiences,
    getUserEducations,
    getUserSkills,
    getUserContacts,
  }
}
