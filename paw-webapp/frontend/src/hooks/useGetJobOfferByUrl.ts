import { useRequestApi } from "../api/apiRequest"

export const useGetJobOfferByUrl = () => {
  const { apiRequest } = useRequestApi()

  async function getJobOfferByUrl(jobOfferUrl: string) {
    const response = await apiRequest({
      url: jobOfferUrl,
      method: "GET",
    })
    return response
  }

  return { getJobOfferByUrl }
}
