import { render, screen } from "@testing-library/react";
import Navigation from "../../components/navbar";
import { UserRole } from "../../utils/constants";

const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));


describe('Test Navigation', ()=>{

    test('Test that navigation renders correctly for enterprise role', ()=>{
        render(<Navigation role={UserRole.ENTERPRISE}/>)
        expect(screen.getByText("My Recruits")).toBeInTheDocument()
    })

    test('Test that navigation renders correctly for user role', ()=>{
        render(<Navigation role={UserRole.USER}/>)
        expect(screen.getByText("My Applications")).toBeInTheDocument()
    })

})