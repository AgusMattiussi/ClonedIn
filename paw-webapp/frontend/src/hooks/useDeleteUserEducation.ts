import { useRequestApi } from "../api/apiRequest"

export const useDeleteUserEducation = () => {
  const { apiRequest } = useRequestApi()

  async function deleteUserEducation(id: string | undefined, object_id: number) {
    const response = await apiRequest({
      url: `/users/${id}/educations/${object_id}`,
      method: "DELETE",
    })
    return response
  }

  return { deleteUserEducation }
}
