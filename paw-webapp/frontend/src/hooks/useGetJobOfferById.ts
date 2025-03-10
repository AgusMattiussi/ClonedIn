import { useRequestApi } from "../api/apiRequest"

export const useGetJobOfferById = () => {
  const { apiRequest } = useRequestApi()

  async function getJobOfferById(id: string | undefined) {
    const response = await apiRequest({
      url: `/jobOffers/${id}`,
      method: "GET",
      requiresAuth: true,
    })
    return response
  }

  return { getJobOfferById }
}
