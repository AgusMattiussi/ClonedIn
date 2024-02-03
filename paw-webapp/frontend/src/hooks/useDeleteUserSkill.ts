import { useRequestApi } from "../api/apiRequest"

export const useDeleteUserSkill = () => {
  const { apiRequest } = useRequestApi()

  async function deleteUserSkill(id: string | undefined, object_id: number) {
    const response = await apiRequest({
      url: `/users/${id}/skills/${object_id}`,
      method: "DELETE",
    })
    return response
  }

  return { deleteUserSkill }
}
