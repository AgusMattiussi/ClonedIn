import { renderHook, act } from '@testing-library/react';
import { useRequestApi } from '../../api/apiRequest'; 
import { useGetEnterpriseById } from '../../hooks/useGetEnterpriseById';

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetEnterpriseById', () => {
  it('fetches enterprise successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetEnterpriseById());

    // Mock API request response
    const mockResponse = { status: 200, data: [
        {
            "description": "Empresa de bian",
            "email": "britorto+1@itba.edu.ar",
            "id": 1,
            "links": {
                "category": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/2",
                "contacts": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/contacts?enterpriseId=1",
                "image": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/enterprises/1/image",
                "jobOffers": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/jobOffers?enterpriseId=1",
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/enterprises/1"
            },
            "name": "Bianterprise",
            "website": "",
            "workers": "No-Especificado"
        }
  ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    let enterprise;
    await act(async () => {
      enterprise = await result.current.getEnterpriseById("1");
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
      url: '/enterprises/1',
      method: 'GET',
      requiresAuth: true,
    });
    expect(enterprise).toEqual(mockResponse);
  });

  it('fails to fetch enterprise', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetEnterpriseById());

    // Mock API request response
    const mockResponse = { status: 404, data: [
        {
            "detail": "The enterprise with id 42 was not found or does not exist.",
            "errorClass": "EnterpriseNotFoundException",
            "message": "Enterprise not found"
        }
  ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    let enterprise;
    await act(async () => {
      enterprise = await result.current.getEnterpriseById("42");
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
      url: '/enterprises/42',
      method: 'GET',
      requiresAuth: true,
    });
    expect(enterprise).toEqual(mockResponse);
  });
});