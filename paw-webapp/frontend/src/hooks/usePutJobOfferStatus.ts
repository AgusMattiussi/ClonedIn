import { useRequestApi } from "../api/apiRequest"

export const usePutJobOfferStatus = () => {
  const { apiRequest } = useRequestApi()

  async function closeJobOffer(
    id: string | undefined,
    jobOfferToCloseId: number | null,
    queryParams: Record<string, string> = {},
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}/jobOffers/${jobOfferToCloseId}`,

      method: "PUT",
      queryParams: queryParams,
    })
    return response
  }

  return { closeJobOffer }
}
