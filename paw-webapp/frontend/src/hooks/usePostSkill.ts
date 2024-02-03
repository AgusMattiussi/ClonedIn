import { useRequestApi } from "../api/apiRequest"

export const usePostSkill = () => {
  const { apiRequest } = useRequestApi()

  async function addSkill(id: string | undefined, skill: string) {
    const response = await apiRequest({
      url: `/users/${id}/skills`,
      method: "POST",
      body: {
        skill,
      },
    })
    return response
  }

  return { addSkill }
}
