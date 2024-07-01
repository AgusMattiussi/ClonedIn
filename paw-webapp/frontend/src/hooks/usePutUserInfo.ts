import { useRequestApi } from "../api/apiRequest"

export const usePutUserInfo = () => {
  const { apiRequest } = useRequestApi()

  async function modifyUserInfo(
    id: string | undefined,
    name: string,
    location: string,
    currentPosition: string,
    description: string,
    category: string | undefined,
    educationLevel: string | undefined,
  ) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "POST",
      body: {
        name,
        location,
        currentPosition,
        description,
        category,
        educationLevel,
      },
    })
    return response
  }

  return { modifyUserInfo }
}
