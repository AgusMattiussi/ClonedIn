import { useRequestApi } from "../api/apiRequest"

export const usePostJobOfferStatus = () => {
  const { apiRequest } = useRequestApi()

  async function closeJobOffer(
    id: number | null,
    availability: string,
  ) {
    const response = await apiRequest({
      url: `/jobOffers/${id}`,
      method: "POST",
      body: {
        availability
      },
    })
    return response
  }

  return { closeJobOffer }
}
