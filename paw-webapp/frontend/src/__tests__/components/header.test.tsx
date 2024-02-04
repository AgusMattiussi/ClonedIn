import { render, screen } from "@testing-library/react";
import Header from "../../components/header";

const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));

describe('Test Header', ()=>{

    test('Test that modal renders correctly', ()=>{
        render(<Header/>)
        expect(screen.getByAltText("logo")).toBeInTheDocument()
    })
    
})