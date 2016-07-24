package org.census.webapp.web.controllers;

import org.census.commons.dto.personnel.PositionDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 11.05.12)
*/

@Controller
@RequestMapping("/personnel")
@SessionAttributes
public class PositionsController {

 @RequestMapping(value = "/addPosition", method = RequestMethod.POST)
 public String addPosition(@ModelAttribute("contact")PositionDto position, BindingResult result) {
  System.out.println("position -> " + position);
  return "redirect:positions.cns";
 }

 @RequestMapping("/positions")
 public ModelAndView showPositions() {
  return new ModelAndView("personnel/admin/position", "command", new PositionDto());
 }

}