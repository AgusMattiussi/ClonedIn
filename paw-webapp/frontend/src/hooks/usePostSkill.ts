import { useRequestApi } from "../api/apiRequest"

export const usePostSkill = () => {
  const { apiRequest } = useRequestApi()

  async function addSkill(id: string | undefined, description: string) {
    const response = await apiRequest({
      url: `/users/${id}/skills`,
      method: "POST",
      body: {
        description,
      },
    })
    return response
  }

  return { addSkill }
}
