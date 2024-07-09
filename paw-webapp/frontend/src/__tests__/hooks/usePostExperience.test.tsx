import { renderHook, act } from '@testing-library/react';
import { usePostExperience } from '../../hooks/usePostExperience';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('usePostExperience', () => {
  it('creates experience successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostExperience());

    // Mock API request response
    const mockResponse = { status: 201, data: { message: 'Experience created successfully' } };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"
    const enterpriseName = "Exampleton Enterprise"
    const position = "Example Poisition"
    const description = "This is an example description"
    const yearFrom = "2017"
    const monthFrom = "3"
    const yearTo = "2022"
    const monthTo = "12"
   
    let postResult;
    await act(async () => {
      postResult = await result.current.addExperience(id, enterpriseName, position, description, monthFrom, yearFrom, monthTo, yearTo);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/users/${id}/experiences`,
        method: "POST",
        body: {
          enterpriseName,
          position,
          description,
          monthFrom,
          yearFrom,
          monthTo,
          yearTo,
        },
    });
    expect(postResult).toEqual(mockResponse);
  });
});