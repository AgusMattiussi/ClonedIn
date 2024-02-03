import { useRequestApi } from "../api/apiRequest"

export const usePutUserContactStatus = () => {
  const { apiRequest } = useRequestApi()

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

  return { answerUserContact }
}
