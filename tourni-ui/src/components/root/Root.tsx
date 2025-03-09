import React from 'react';
import { Outlet } from 'react-router';
import Header from '../header/Header';
import Footer from '../footer/Footer';

const Root: React.FC = () => (
  <>
    <Header />
    <Outlet />
    <Footer />
  </>
);

export default Root;
