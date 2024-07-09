import { renderHook, act } from '@testing-library/react';
import { useGetUserEducations } from '../../hooks/useGetUserEducations';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetUserEducations', () => {
  it('fetches user educations successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetUserEducations ());

    // Mock API request response
    const mockResponse = { status: 200, data: [
        {
            "description": "Specialized in penalty kicks",
            "id": 17,
            "institutionName": "Instituto Nacional de Independiente",
            "links": {
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28/educations/17",
                "user": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28"
            },
            "monthFrom": 8,
            "monthTo": 8,
            "title": "Sport Studies",
            "yearFrom": 2010,
            "yearTo": 2015
        }
    ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"

    let getResult;
    await act(async () => {
      getResult = await result.current.getUserEducations(id);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/users/${id}/educations`,
        method: "GET",
        requiresAuth: true,
    });
    expect(getResult).toEqual(mockResponse);
  });
});