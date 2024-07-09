import { renderHook, act } from '@testing-library/react';
import { useGetEnterpriseContacts } from '../../hooks/useGetEnterpriseContacts';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetEnterpriseContacts', () => {
  it('fetches enterprise contacts successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetEnterpriseContacts());

    // Mock API request response
    const mockResponse = { status: 200, data: [
        {
            "date": "06/07/2024",
            "filledBy": "enterprise",
            "id": "28-29",
            "links": {
                "enterprise": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/enterprises/29",
                "jobOffer": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/jobOffers/29",
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/contacts/28-29",
                "user": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28",
                "userCategory": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/10"
            },
            "status": "aceptada",
            "userId": 28,
            "userName": "Emiliano Martinez",
            "userYearsOfExp": 8
        }
    ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    let categories;
    await act(async () => {
      categories = await result.current.getEnterpriseContacts();
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
      url: '/contacts',
      method: 'GET',
      queryParams: {},
      requiresAuth: true,
    });
    expect(categories).toEqual(mockResponse);
  });
});