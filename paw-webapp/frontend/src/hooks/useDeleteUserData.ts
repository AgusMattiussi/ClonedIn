import { useRequestApi } from "../api/apiRequest"

export const useDeleteUserData = () => {
  const { apiRequest } = useRequestApi()

  async function deleteUserExperience(id: string | undefined, object_id: number) {
    const response = await apiRequest({
      url: `/users/${id}/experiences/${object_id}`,
      method: "DELETE",
    })
    return response
  }

  async function deleteUserEducation(id: string | undefined, object_id: number) {
    const response = await apiRequest({
      url: `/users/${id}/educations/${object_id}`,
      method: "DELETE",
    })
    return response
  }

  async function deleteUserSkill(id: string | undefined, object_id: number) {
    const response = await apiRequest({
      url: `/users/${id}/skills/${object_id}`,
      method: "DELETE",
    })
    return response
  }

  return { deleteUserExperience, deleteUserEducation, deleteUserSkill }
}
