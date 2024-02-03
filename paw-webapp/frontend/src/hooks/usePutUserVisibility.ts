import { useRequestApi } from "../api/apiRequest"

export const usePutUserVisibility = () => {
  const { apiRequest } = useRequestApi()

  async function modifyUserVisibility(id: string | undefined, visibility: string) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "PUT",
      body: { visibility },
    })
    return response
  }

  return { modifyUserVisibility }
}
