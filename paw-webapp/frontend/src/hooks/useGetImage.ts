import { useRequestApi } from "../api/apiRequest"

export const useGetImage = () => {
  const { apiRequest } = useRequestApi()

  async function getImageByUrl(imageUrl: string) {
    const response = await apiRequest({
      url: imageUrl,
      method: "GET",
    })
    return response
  }

  return { getImageByUrl }
}
