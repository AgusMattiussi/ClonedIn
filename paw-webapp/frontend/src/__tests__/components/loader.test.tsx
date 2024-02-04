import { render, screen } from "@testing-library/react";
import Loader from "../../components/loader";

describe('Test Loader', ()=>{

    test('Test that modal renders correctly', ()=>{
        render(<Loader/>)
        expect(screen.getByText("Loading")).toBeInTheDocument()
    })
    
})