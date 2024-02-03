import { useRequestApi } from "../api/apiRequest"

export const useDeleteUserExperience = () => {
  const { apiRequest } = useRequestApi()

  async function deleteUserExperience(id: string | undefined, object_id: number) {
    const response = await apiRequest({
      url: `/users/${id}/experiences/${object_id}`,
      method: "DELETE",
    })
    return response
  }

  return { deleteUserExperience }
}
