import { renderHook, act } from '@testing-library/react';
import { useDeleteUserEducation } from '../../hooks/useDeleteUserEducation';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
  }));
  
  describe('useDeleteUserEducation', () => {
    it('deletes user education successfully', async () => {
      const mockApiRequest = jest.fn();
      (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });
  
      const { result } = renderHook(() => useDeleteUserEducation());
  
      // Mock API request response
      const mockResponse = { status: 200, data: { message: 'Education deleted successfully' } };
      mockApiRequest.mockResolvedValue(mockResponse);
  
      let deleteResult;
      await act(async () => {
        deleteResult = await result.current.deleteUserEducation('user_id_123', 456);
      });
  
      expect(mockApiRequest).toHaveBeenCalledWith({
        url: '/users/user_id_123/educations/456',
        method: 'DELETE',
      });
      expect(deleteResult).toEqual(mockResponse);
    });
  });
