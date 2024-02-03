import { fireEvent, render, screen } from "@testing-library/react";
import Header from "../header";

describe('Test Header', ()=>{

    test('Test that modal renders correctly', ()=>{
        render(<Header/>)
        expect(screen.getByAltText("logo")).toBeInTheDocument()
    })
    
})