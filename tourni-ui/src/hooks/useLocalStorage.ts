const useLocalStorage = (key:string) => {
  const setItem = (value: unknown) => {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error('Error saving to local storage', error);
    }
  };

  const getItem = () => {
    try {
      const item = localStorage.getItem(key);
      return item ? JSON.parse(item) : null;
    } catch (error) {
      console.error('Error reading from local storage', error);
      return null;
    }
  };

  const removeItem = () => {
    try {
      localStorage.removeItem(key);
    } catch (error) {
      console.error('Error removing from local storage', error);
    }
  };

  const clear = () => {
    try {
      localStorage.clear();
    } catch (error) {
      console.error('Error clearing local storage', error);
    }
  };

  return {
    setItem, getItem, removeItem, clear,
  };
};

export default useLocalStorage;
