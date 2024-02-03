import { useRequestApi } from "../api/apiRequest"

export const usePutEnterpriseContactStatus = () => {
  const { apiRequest } = useRequestApi()

  async function answerEnterpriseContact(
    id: string | undefined,
    jobOfferToAnswerId: string,
    userToAnswerId: string,
    queryParams: Record<string, string> = {},
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}/contacts/${jobOfferToAnswerId}/${userToAnswerId}`,
      method: "PUT",
      queryParams: queryParams,
    })
    return response
  }

  return { answerEnterpriseContact }
}
