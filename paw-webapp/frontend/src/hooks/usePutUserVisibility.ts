import { useRequestApi } from "../api/apiRequest"

export const usePutUserVisibility = () => {
  const { apiRequest } = useRequestApi()

  async function modifyUserVisibility(id: string | undefined, visibility: boolean) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "POST",
      body: { visibility },
    })
    return response
  }

  return { modifyUserVisibility }
}
