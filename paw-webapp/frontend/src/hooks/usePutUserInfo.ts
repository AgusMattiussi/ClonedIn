import { useRequestApi } from "../api/apiRequest"

export const usePutUserInfo = () => {
  const { apiRequest } = useRequestApi()

  async function modifyUserInfo(
    id: string | undefined,
    name: string,
    location: string,
    position: string,
    aboutMe: string,
    category: string | undefined,
    level: string | undefined,
  ) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "PUT",
      body: {
        name,
        location,
        position,
        aboutMe,
        category,
        level,
      },
    })
    return response
  }

  return { modifyUserInfo }
}
