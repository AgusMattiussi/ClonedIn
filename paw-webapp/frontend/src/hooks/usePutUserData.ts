import { useRequestApi } from "../api/apiRequest"

export const usePutUserData = () => {
  const { apiRequest } = useRequestApi()

  async function putUserVisibility(id: string | undefined, visibility: string) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "PUT",
      body: { visibility },
    })
    return response
  }

  return { putUserVisibility }
}
