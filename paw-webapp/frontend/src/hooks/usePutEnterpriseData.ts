import { useRequestApi } from "../api/apiRequest"

export const usePutUserData = () => {
  const { apiRequest } = useRequestApi()

  async function closeJobOffer(
    id: string | undefined,
    jobOfferToCloseId: string,
    queryParams: Record<string, string> = {},
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}/jobOffers/${jobOfferToCloseId}`,

      method: "PUT",
      queryParams: queryParams,
    })
    return response
  }

  async function cancelJobOffer(
    id: string | undefined,
    jobOfferToCancelId: string,
    userToCancelId: string,
    queryParams: Record<string, string> = {},
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}/contacts/${jobOfferToCancelId}/${userToCancelId}`,
      method: "PUT",
      queryParams: queryParams,
    })
    return response
  }

  return { closeJobOffer, cancelJobOffer }
}
