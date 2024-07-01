import { useRequestApi } from "../api/apiRequest"

export const usePutUserVisibility = () => {
  const { apiRequest } = useRequestApi()

  async function modifyUserVisibility(id: string | undefined, visible: boolean) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "POST",
      body: { visible },
    })
    return response
  }

  return { modifyUserVisibility }
}
