import { useRequestApi } from "../api/apiRequest"

export const usePutUserData = () => {
  const { apiRequest } = useRequestApi()

  async function modifyUserVisibility(id: string | undefined, visibility: string) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "PUT",
      body: { visibility },
    })
    return response
  }

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

  async function answerUserContact(
    id: string | undefined,
    jobOfferToAnswerId: string,
    queryParams: Record<string, string> = {},
  ) {
    const response = await apiRequest({
      url: `/users/${id}/contacts/${jobOfferToAnswerId}`,
      method: "PUT",
      queryParams: queryParams,
    })
    return response
  }

  return { modifyUserVisibility, modifyUserInfo, answerUserContact }
}
